package cn.wecloud.storage.cloud.down;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.EndConsumerHelper;

/**
 * Created by Chauncey on 2018/6/14 16:08.
 * 下载监听类
 */
public abstract class DownloadObserver<T> implements Observer<T> {
    private Disposable s;

    @Override
    public final void onSubscribe(@NonNull Disposable s) {
        if (EndConsumerHelper.validate(this.s, s, getClass())) {
            this.s = s;
            onStart();
        }
    }

    /**
     * Cancels the upstream's disposable.
     */
    public void cancel() {
        Disposable s = this.s;
        this.s = DisposableHelper.DISPOSED;
        s.dispose();
    }

    /**
     * Called once the subscription has been set on this observer; override this
     * to perform initialization.
     */
    protected void onStart() {
    }

}
