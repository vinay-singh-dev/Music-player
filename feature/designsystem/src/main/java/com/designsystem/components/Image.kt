package com.designsystem.components

import android.graphics.drawable.Drawable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.designsystem.R
import com.designsystem.theme.DeepOrange

/**
 * A method for the load image from the network in an animated way.
 * This method displays a circular progress indicator while loading image from the network.
 * If loading the image fail, method displays an error image.
 *
 *  @param modifier the [Modifier] to be applied for this method.
 *  @param imageUrl url of the image.
 */
@Composable
fun AnimatedImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentScale: ContentScale = ContentScale.Crop,
    onPainterStateSuccess: (Drawable) -> Unit = {}
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .allowHardware(false)
            .build()
    )

    val transition by animateFloatAsState(
        targetValue = if (painter.state is AsyncImagePainter.State.Success) 1f else 0f,
        label = "image transition animation"
    )

    val matrix = ColorMatrix()
    matrix.setToSaturation(transition)

    when (val image = painter.state) {
        is AsyncImagePainter.State.Loading -> {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = DeepOrange)
            }
        }

        is AsyncImagePainter.State.Error -> {
            ErrorImage(modifier)
        }

        is AsyncImagePainter.State.Empty -> {
            EmptyImage(modifier)
        }

        is AsyncImagePainter.State.Success -> {
            onPainterStateSuccess(image.result.drawable)
            Image(
                modifier = modifier
                    .scale(.8f + (.2f * transition))
                    .graphicsLayer { rotationX = (1f - transition) * 5f }
                    .alpha(1f.coerceAtMost(transition / .2f)),
                painter = painter,
                contentDescription = "Artist image",
                contentScale = contentScale,
                colorFilter = ColorFilter.colorMatrix(colorMatrix = matrix)
            )
        }
    }
}

@Composable
private fun EmptyImage(modifier: Modifier) {
    Image(
        modifier = modifier,
        contentDescription = "No Image Available",
        contentScale = ContentScale.Crop,
        painter = painterResource(id = R.drawable.no_image_available)
    )
}

@Composable
private fun ErrorImage(modifier: Modifier) {
    Image(
        modifier = modifier,
        contentDescription = "The image could not be loaded.",
        contentScale = ContentScale.Fit,
        painter = painterResource(id = R.drawable.error_image)
    )
}