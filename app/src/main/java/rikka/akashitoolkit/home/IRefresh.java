package rikka.akashitoolkit.home;

import retrofit2.Call;

/**
 * Created by Rikka on 2016/8/31.
 */
public interface IRefresh<T> {
    void onRefresh(Call<T> call, boolean force_cache);

    void onSuccess(T data);

    void onFailure(Call<T> call, Throwable t);
}
