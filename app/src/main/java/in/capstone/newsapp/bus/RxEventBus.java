package in.capstone.newsapp.bus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by bhaskar.gangabathina on 23-12-2016.
 */

public class RxEventBus {

    private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

    public void post(Object o) {
        bus.onNext(o);
    }

    public Observable<Object> getBusObservable() {
        return bus;
    }
}
