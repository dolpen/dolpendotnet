package net.dolpen.gae.libs.servlet.binder;

/**
 * Created by yamada on 2014/09/19.
 */
public interface TypeBinder<T> {

    public T bind(String[] src);

}
