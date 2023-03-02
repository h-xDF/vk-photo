import com.vk.api.sdk.client.TransportClient
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.ServiceActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import com.vk.api.sdk.objects.photos.PhotoSizes
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import java.io.File
import java.nio.file.Paths

fun main(args: Array<String>) {

    val appID = 123
    val clientSecret = ""
    val serviceSecret = ""
    val redirectURI = ""

    val communityID = -63282215
    val communityShortTitle = "nercnews"

    val photoTypeArr = arrayOf("w","z","y")

    val transportClient: TransportClient = HttpTransportClient()
    val vk = VkApiClient(transportClient)


    val serviceActor = ServiceActor(appID, serviceSecret)

    // возвращает количество и id альбомов в группе
    val respAlbum = vk.photos()
        .getAlbums(serviceActor)
        .needSystem(true)
        .ownerId(communityID)
        .execute()

    // возвращает информацию о группе
    val titleGroup = vk.groups()
        .getByIdObjectLegacy(serviceActor)
        .groupId("63282215")
        .execute()

    // возвращает информацию о фотках по альбому
    val respGetPhoto = vk.photos()
        .get(serviceActor)
        .ownerId(communityID)
        .albumId("289099425")
        .photoIds()
        .photoSizes(true)
        .offset(700)
        .execute()

    val photoUrl = respGetPhoto.items.map { it.sizes }

    val sortPhotoUrl = respGetPhoto.items.map { it.sizes }
        .map { it.maxBy { it.type } }


    // create album folder
    val folder = Paths.get("New_Album").toFile()
    folder.mkdir()

    sortPhotoUrl.forEach {
        val urlPhoto = it.url
    }
//    respGetPhoto.items.forEach {
//        it.t
//    }


    savePhoto(sortPhotoUrl, folder)
    println("end_program")
}

//added jpeg format
fun savePhoto(list: List<PhotoSizes>, folder: File) {
    val client = HttpClients.createDefault()
    val filePhoto = File(folder, "Photo_1")

    client
        .execute(HttpGet(list.first().url))
        .entity
        .writeTo(filePhoto.outputStream())
}


