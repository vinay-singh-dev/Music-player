package com.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.designsystem.icons.DeezerIcons
import com.designsystem.theme.PlayerRed

val PlayerHeight = 80.dp
private val PlayerShape = RoundedCornerShape(topStartPercent = 20, topEndPercent = 20)
private val PlayerColor = PlayerRed
private val PlayStopButtonSize = 36.dp
private val CloseButtonSize = 30.dp

@Composable
fun MusicPlayer(
    modifier: Modifier = Modifier,
    songName: String,
    songArtist: String,
    onCloseClicked: () -> Unit,
    onPlayButtonClicked: () -> Unit,
    isAudioPlaying: Boolean
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(PlayerHeight),
            shape = PlayerShape,
            colors = CardDefaults.cardColors(containerColor = PlayerColor)
        ) {
            Row(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = modifier.weight(8f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlayerButton(
                        modifier = modifier.size(PlayStopButtonSize),
                        onPlayButtonClicked = onPlayButtonClicked,
                        isAudioPlaying = isAudioPlaying
                    )
                    SongDetail(
                        modifier = modifier
                            .fillMaxHeight()
                            .padding(start = 16.dp),
                        songName = songName,
                        songArtist = songArtist
                    )
                }
                ClosePlayerButton(
                    modifier = modifier
                        .weight(2f)
                        .fillMaxSize(),
                    onCloseClicked = onCloseClicked
                )
            }
        }
    }
}

@Composable
private fun SongDetail(modifier: Modifier, songName: String, songArtist: String) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        SongName(songName = songName)
        SongArtist(songArtist = songArtist)
    }
}

@Composable
private fun SongArtist(songArtist: String) {
    Text(
        text = songArtist,
        style = MaterialTheme.typography.labelSmall.copy(color = Color.White),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun SongName(songName: String) {
    Text(
        text = songName,
        style = MaterialTheme.typography.labelMedium.copy(color = Color.White),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun PlayerButton(
    modifier: Modifier,
    onPlayButtonClicked: () -> Unit,
    isAudioPlaying: Boolean
) {
    IconButton(
        onClick = {
            onPlayButtonClicked()
        }
    ) {
        Icon(
            modifier = modifier,
            imageVector = if (isAudioPlaying) {
                DeezerIcons.Pause
            } else {
                DeezerIcons.Play
            },
            tint = Color.White,
            contentDescription = null
        )
    }
}

@Composable
private fun ClosePlayerButton(modifier: Modifier, onCloseClicked: () -> Unit) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterEnd
    ) {
        IconButton(onClick = onCloseClicked) {
            Icon(
                modifier = Modifier.size(CloseButtonSize),
                imageVector = DeezerIcons.Close,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}