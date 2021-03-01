package net.yakclient.mixin.internal.loader;

import java.io.IOException;
import java.io.InputStream;

public class TargetClassLoader extends ProxyClassLoader {
    private final PackageTarget target;
    private final ProxyClassLoader lazyParent;

    public TargetClassLoader(ProxyClassLoader parent, PackageTarget target) {
        super(null);
        this.lazyParent = parent;
        this.target = target;
    }

    @Override
    protected final Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        final ClassTarget target = ClassTarget.create(name);
        if (!this.target.isTargetOf(target)) return this.lazyParent.loadClass(name, resolve);
        try {
            Class<?> c = this.findLoadedClass(name);

            if (c == null) {
                final InputStream classData = this.getResourceAsStream(name.replace('.', '/') + ".class");
                if (classData == null) throw new ClassNotFoundException("Failed to find class " + name);

                final byte[] bytes = new byte[classData.available()];

                c = this.defineClass(name, bytes, 0, classData.read(bytes));
            }
            if (resolve) this.resolveClass(c);

            return c;
        } catch (IOException e) {
            throw new ClassNotFoundException(e.getMessage());
        }
    }
}