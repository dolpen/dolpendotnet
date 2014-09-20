package net.dolpen.gae.libs.servlet.binder;

public interface TypeBinder<T> {

    public T bind(String[] src);

}
