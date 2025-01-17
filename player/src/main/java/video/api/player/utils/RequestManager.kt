package video.api.player.utils

import android.graphics.Bitmap
import android.widget.ImageView.ScaleType
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.NoCache
import video.api.player.models.PlayerJsonRequest
import video.api.player.models.SessionTokenRequest
import video.api.player.models.VideoType


object RequestManager {
    private val queue = RequestQueue(NoCache(), BasicNetwork(HurlStack())).apply { start() }

    fun getSessionToken(
        url: String,
        videoType: VideoType,
        onSuccess: (String?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val sessionTokenRequest = if (videoType == VideoType.VOD) {
            SessionTokenRequest(url,
                { sessionTokenResult ->
                    onSuccess(sessionTokenResult.sessionToken)
                },
                { error ->
                    onError(error)
                }
            )
        } else {
            PlayerJsonRequest(url,
                { sessionTokenResult ->
                    onSuccess(sessionTokenResult.sessionToken)
                },
                { error ->
                    onError(error)
                }
            )
        }

        queue.add(sessionTokenRequest)
    }

    fun getImage(
        url: String,
        maxWidth: Int,
        maxHeight: Int,
        scaleType: ScaleType,
        onSuccess: (Bitmap) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val imageRequest = ImageRequest(url,
            { bitmap ->
                onSuccess(bitmap)
            },
            maxWidth,
            maxHeight,
            scaleType,
            Bitmap.Config.RGB_565,
            { error ->
                onError(error)
            }
        )

        queue.add(imageRequest)
    }
}