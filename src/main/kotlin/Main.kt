import com.vk.api.sdk.client.TransportClient
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.ServiceActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import com.vk.api.sdk.objects.photos.PhotoSizes
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import screen.MainScreen
import java.io.File
import java.io.FileInputStream
import java.nio.file.Paths
import java.util.Properties

fun main(args: Array<String>) {

    MainScreen().runScreen()


    val prop = Properties()
    prop.load(FileInputStream("src/main/resources/local.properties"))

    val appID = prop.getProperty("appID").toInt()
    val serviceSecret = prop.getProperty("serviceSecret")

    val groupID = -63282215
    val communityShortTitle = "nercnews"

    val vkClient = VkClient(appID = appID, secret = serviceSecret)
    vkClient.infoGroup(groupID)

    val transportClient: TransportClient = HttpTransportClient()
    val vk = VkApiClient(transportClient)

    val serviceActor = ServiceActor(appID, serviceSecret)

    // возвращает количество и id альбомов в группе
    val respAlbum = vk.photos()
        .getAlbums(serviceActor)
        .needSystem(true)
        .ownerId(groupID)
        .execute()

    // возвращает информацию о группе
    val titleGroup = vk.groups()
        .getByIdObjectLegacy(serviceActor)
        .groupId("63282215")
        .execute()

    // возвращает информацию о фотках по альбому
    val respGetPhoto = vk.photos()
        .get(serviceActor)
        .ownerId(groupID)
        .albumId("289099425")
        .photoIds()
        .photoSizes(true)
        .offset(0)
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


