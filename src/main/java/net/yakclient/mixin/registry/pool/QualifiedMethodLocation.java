package net.yakclient.mixin.registry.pool;

import net.yakclient.mixin.api.InjectionType;
import net.yakclient.mixin.registry.MixinMetaData;

import java.util.Objects;

public class QualifiedMethodLocation {
    private final MethodLocation location;
    private final InjectionType injectionType;
    private final int priority;

    public QualifiedMethodLocation(Class<?> cls, String method, InjectionType injectionType, int priority) {
        this.location = new MethodLocation(cls, method);
        this.injectionType = injectionType;
        this.priority = priority;
    }

    public static QualifiedMethodLocation fromDataDest(MixinMetaData data) {
        return new QualifiedMethodLocation(data.getClassTo(), data.getMethodTo(), data.getType(), data.getPriority());
    }

    public static QualifiedMethodLocation fromDataOrigin(MixinMetaData data) {
        return new QualifiedMethodLocation(data.getClassFrom(), data.getMethodFrom(), data.getType(), data.getPriority());
    }


    public InjectionType getInjectionType() {
        return injectionType;
    }

    public int getPriority() {
        return priority;
    }

    public Class<?> getCls() {
        return this.location.getCls();
    }

    public String getMethod() {
        return this.location.getMethod();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QualifiedMethodLocation that = (QualifiedMethodLocation) o;
        return priority == that.priority && Objects.equals(location, that.location) && injectionType == that.injectionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, injectionType, priority);
    }
}
