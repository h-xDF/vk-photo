package screen

import VkClient
import com.vk.api.sdk.objects.photos.PhotoAlbumFull

class DownloadScreen(
    private val vkClient: VkClient,
    private val groupId: Int,
    private val album: PhotoAlbumFull
) {
    fun runScreen() {
        initData()
    }

    private fun initData() {
        val photoResp = vkClient.getItemPhotos(groupId = groupId, albumId = album.id.toString())
    }
}