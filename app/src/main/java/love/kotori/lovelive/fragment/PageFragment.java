package love.kotori.lovelive.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import love.kotori.lovelive.domain.Api;
import love.kotori.lovelive.R;
import love.kotori.lovelive.util.HttpUtil;
import love.kotori.lovelive.util.ResponseHandleUtil;
import love.kotori.lovelive.util.SharedPreferencesUtil;
import love.kotori.lovelive.view.ImageViewerActivity;
import love.kotori.lovelive.adapter.ImageRecyclerViewAdapter;
import love.kotori.lovelive.model.Image;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmResults;


public class PageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    private ImageRecyclerViewAdapter imageRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RealmResults<Image> images;

    private final int PRELOAD_SIZE = 6;
    private int mPage;

    private String httpResponse;
    private final int LOAD_IMAGE_COUNT = 100;
    private final int ONCE_LOAD_NUMBER = 20;
    private Realm realm;
    private boolean isFirstRequest = true;
    private static final String TYPE_ARGS_KEY = "TYPE_KEY";
    private int type = 0;


    public static PageFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(TYPE_ARGS_KEY, type);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt(TYPE_ARGS_KEY);
        realm = Realm.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.content);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresher);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);
        mPage = SharedPreferencesUtil.getCurrentPage(getActivity(), type);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

        if (images.isEmpty()) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });

            preHttpRequest();

        } else recyclerView.getAdapter().notifyDataSetChanged();

        recyclerView.addOnScrollListener(getOnBottomListener(staggeredGridLayoutManager));
    }



    private void init() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                forceRefresh();
            }
        });


        images = realm.where(Image.class)
                .equalTo("type", type).findAll();

        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        imageRecyclerViewAdapter = new ImageRecyclerViewAdapter(getActivity(), images) {
            @Override
            protected void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(), ImageViewerActivity.class);
                intent.putExtra("CURRENT_INDEX", position);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        };
        recyclerView.setAdapter(imageRecyclerViewAdapter);

    }

    private void preHttpRequest() {
        HttpUtil.HttpUtilCallBack httpUtilCallBack = null;
        String url = Api.getApiUrl(type, mPage, LOAD_IMAGE_COUNT);

        httpUtilCallBack = getHttpUtilCallBack();
        getImagesDataFromHttp(url, httpUtilCallBack);
    }

    private void forceRefresh(){
        isFirstRequest = true;
        mPage = 1;
        realm.beginTransaction();
        realm.where(Image.class)
                .equalTo("type", type).findAll().clear();
        realm.commitTransaction();
        preHttpRequest();
    }

    @NonNull
    private HttpUtil.HttpUtilCallBack getHttpUtilCallBack() {

        return new HttpUtil.HttpUtilCallBack() {
            @Override
            public void onFinish(String response) {
                httpResponse = response;
                isFirstRequest = false;
                handleResponse();
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "加载失败！", Toast.LENGTH_SHORT).show();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        };

    }

    private void getImagesDataFromHttp(String url, HttpUtil.HttpUtilCallBack httpUtilCallBack) {

        if (isFirstRequest) {
            HttpUtil.httpRequest(getActivity(), url, httpUtilCallBack);
        } else {
            handleResponse();
        }
    }

    private void handleResponse() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ResponseHandleUtil.HandleSearchResponseFromHttp(getActivity(), httpResponse, (mPage - 1) * ONCE_LOAD_NUMBER, ONCE_LOAD_NUMBER, type);
                } catch (ExecutionException | InterruptedException | JSONException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "加载失败，请再次尝试", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //recyclerView.getAdapter().notifyDataSetChanged();
                        if (mPage * ONCE_LOAD_NUMBER - ONCE_LOAD_NUMBER - 1 > 0)
                            recyclerView.getAdapter().notifyItemRangeChanged(mPage * ONCE_LOAD_NUMBER - ONCE_LOAD_NUMBER - 1, ONCE_LOAD_NUMBER);
                        else
                            recyclerView.getAdapter().notifyDataSetChanged();
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });
            }
        }).start();

    }

    private RecyclerView.OnScrollListener getOnBottomListener(final StaggeredGridLayoutManager layoutManager) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                boolean isBottom = layoutManager.findLastCompletelyVisibleItemPositions(new int[2])[1] >= imageRecyclerViewAdapter.getItemCount() - PRELOAD_SIZE;
                if (!swipeRefreshLayout.isRefreshing() && isBottom) {
                    swipeRefreshLayout.setRefreshing(true);
                    mPage++;
                    preHttpRequest();

                }
            }
        };
    }

    @Override
    public void onRefresh() {
        Log.d("test", "NicoNicoNi");
        swipeRefreshLayout.setRefreshing(true);
        forceRefresh();
    }

    @Override
    public void onDestroy() {
        realm.close();
        SharedPreferencesUtil.saveCurrentImagePositionAndPage(getActivity(), mPage, type);
        super.onDestroy();
    }
}
