package screen

import InputUtils
import VkClient
import com.vk.api.sdk.objects.photos.PhotoAlbumFull
import com.vk.api.sdk.objects.photos.PhotoSizes
import java.io.File
import java.net.URL
import java.nio.file.Paths
import javax.imageio.ImageIO

class DownloadScreen(
    private val vkClient: VkClient,
    private val groupId: Int,
    private val album: PhotoAlbumFull
) {

    private lateinit var utils: InputUtils
    fun runScreen() {
        utils = InputUtils()

        initData()
    }

    private fun initData() {
        val photoResp = vkClient.getItemPhotos(groupId = groupId, albumId = album.id.toString())

        var offset = 0
        while (offset < photoResp!!.count) {
            val response = vkClient.getItemPhotos(groupId, album.id.toString(), offset)

            if (response!!.items.isEmpty()) break

            val sortedList = response.items.map { it.sizes }
                .map { it.maxBy { it.type } }

            val folder = Paths.get("target/${album.title.replace(":", "")}").toFile()
            folder.mkdir()

            savePhoto(sortedList, folder)
            utils.showDownloadProgress("Successfully loaded photos from id=$offset")
            offset += 50
        }
    }

    private fun savePhoto(list: List<PhotoSizes>, folder: File) {
        for (link in list) {
            val bufferedImage = ImageIO.read(URL(link.url.toString()).openStream())
            ImageIO.write(bufferedImage, "jpg", File(folder, link.url.path.substringAfterLast("/")))
        }
    }
}