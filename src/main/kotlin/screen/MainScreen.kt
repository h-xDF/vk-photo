package screen

import InputUtils
import VkClient
import com.vk.api.sdk.client.TransportClient
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.ServiceActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import com.vk.api.sdk.objects.photos.PhotoAlbumFull
import java.io.FileInputStream
import java.util.*

class MainScreen {

    private lateinit var utils: InputUtils
    private lateinit var groupName: String

    private lateinit var serviceActor: ServiceActor
    private lateinit var vk: VkApiClient
    private lateinit var vkClient: VkClient

    private var albumList: List<PhotoAlbumFull> = listOf()

    fun runScreen() {

        initData()
        showData()
        waitUserCommand()
    }

    private fun initData() {

        utils = InputUtils()
        val prop = Properties()
        prop.load(FileInputStream("src/main/resources/local.properties"))

        val appID = prop.getProperty("appID").toInt()
        val serviceSecret = prop.getProperty("serviceSecret")
        val groupId = prop.getProperty("groupID").toInt()

        serviceActor = ServiceActor(appID, serviceSecret)

        val transportClient: TransportClient = HttpTransportClient()
        vk = VkApiClient(transportClient)

        vkClient = VkClient(appID = appID, secret = serviceSecret)

        groupName = vkClient.infoGroup(groupId)!!.name

        val albumsResponse = vkClient.getAlbums(idGroup = groupId)
        albumList = albumsResponse?.items ?: emptyList()

    }

    private fun showData() {
        utils.showAlbumMenu(groupName, albumList.map { "${it.title} [${it.size}]" })
    }

    private fun waitUserCommand() {
        val command = utils.readCommand(albumList.size - 1)
        InputUtils().endPrintScreen()
    }

}