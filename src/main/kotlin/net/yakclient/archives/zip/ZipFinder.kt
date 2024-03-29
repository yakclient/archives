package net.yakclient.archives.zip

import net.yakclient.archives.ArchiveFinder
import java.nio.file.Path
import java.util.jar.JarFile
import java.util.zip.ZipFile
import kotlin.reflect.KClass

internal class ZipFinder : ArchiveFinder<ZipReference> {
    override val type: KClass<ZipReference> = ZipReference::class

    override fun find(path: Path): ZipReference {
        return ZipReference(
            JarFile(
                path.toFile().also { assert(it.exists()) },
                true,
                ZipFile.OPEN_READ,
                JarFile.runtimeVersion()
            ), path.toUri()
        )
    }
}