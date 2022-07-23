package net.yakclient.archives

import net.yakclient.archives.Archives.WRITER_FLAGS
import net.yakclient.archives.transform.AwareClassWriter
import net.yakclient.archives.transform.TransformerConfig
import net.yakclient.common.util.resource.ProvidedResource
import net.yakclient.common.util.resource.SafeResource
import org.objectweb.asm.ClassReader
import java.io.Closeable
import java.net.URI

public interface ArchiveReference : Closeable {
    public val location: URI
    public val reader: Reader
    public val writer: Writer
    public val name: String?

    public val modified: Boolean
    public val isClosed: Boolean
    public val isOpen: Boolean
        get() = !isClosed


    public interface Reader {
        public fun of(name: String): Entry?

        public fun contains(name: String): Boolean = get(name) != null

        public fun entries(): Sequence<Entry>

        public operator fun get(name: String): Entry? = of(name)
    }

    public interface Writer {
        public fun put(
            name: String,
            resource: SafeResource,
            handle: ArchiveReference,
            isDirectory: Boolean = false
        ): Unit =
            put(Entry(name, resource, isDirectory, handle))

        public fun put(entry: Entry)

        public fun remove(name: String)
    }

    public data class Entry public constructor(
        public val name: String,
        public val resource: SafeResource,
        public val isDirectory: Boolean,
        public val handle: ArchiveReference,
    ) {
        public fun transform(config: TransformerConfig, handles: List<ArchiveReference> = listOf()): Entry {
            return Entry(
                name,
                ProvidedResource(resource.uri) {
                    Archives.resolve(
                        ClassReader(resource.open()),
                        config,
                        AwareClassWriter(handles + handle, WRITER_FLAGS)
                    )
                },
                isDirectory,
                handle
            )
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Entry) return false

            if (name != other.name) return false

            return true
        }

        override fun hashCode(): Int {
            return name.hashCode()
        }
    }
}