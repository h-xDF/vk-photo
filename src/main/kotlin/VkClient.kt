import com.vk.api.sdk.client.TransportClient
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.ServiceActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import com.vk.api.sdk.objects.groups.responses.GetByIdObjectLegacyResponse
import com.vk.api.sdk.objects.photos.responses.GetAlbumsResponse
import com.vk.api.sdk.objects.photos.responses.GetResponse
import kotlin.math.abs

class VkClient(appID: Int, secret: String) {
    private val serviceActor: ServiceActor = ServiceActor(appID, secret)
    private var vk: VkApiClient
    init {
        val transportClient: TransportClient = HttpTransportClient()
        vk = VkApiClient(transportClient)
    }

    fun infoGroup(idGroup: Int): GetByIdObjectLegacyResponse? {
        return vk.groups()
            .getByIdObjectLegacy(serviceActor)
            .groupId(abs(idGroup).toString()) // 63282215
            .execute()
            .first()
    }

    fun getAlbums(idGroup: Int): GetAlbumsResponse? {
        return vk.photos()
            .getAlbums(serviceActor)
            .needSystem(true)
            .ownerId(idGroup)
            .execute()
    }

    fun getItemPhotos(groupId: Int, albumId: String, startPosition: Int = 0): GetResponse? {
        return vk.photos()
            .get(serviceActor)
            .ownerId(groupId)
            .albumId(albumId) // 289099425
            .photoIds()
            .photoSizes(true)
            .offset(startPosition)
            .execute()
    }
}