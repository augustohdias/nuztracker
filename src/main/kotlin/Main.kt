import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.guto.nuzlocke.core.Core
import dev.guto.nuzlocke.core.data.static.Locations
import dev.guto.nuzlocke.core.data.static.Pokemon
import dev.guto.nuzlocke.core.event.EventBroker
import dev.guto.nuzlocke.core.socket.Server
import kotlinx.coroutines.delay

fun main(args: Array<String>) = application {
    val windowState = rememberWindowState(width = 1600.dp, height = 1080.dp)
    Window(
        onCloseRequest = ::exitApplication,
        title = "NuzTracker 0.0.0.1",
        state = windowState
    ) {
        println("App started.")
        val broker = EventBroker()
        val server = Server(broker)
        server.start()

        val core by remember { mutableStateOf(Core(broker)) }
        core.start()
        MaterialTheme {
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()), Arrangement.spacedBy(5.dp)) {
                Locations.routes().values.forEach {
                    RouteRow(it, core)
                }
            }
            //Row { Text("Route 101") }
        }
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun RouteRow(
    routeName: String,
    core: Core
) {
    val rowHeight = 72.dp

    var isHover by remember { mutableStateOf(false) }
    var pokedexNumber by remember { mutableStateOf(Pokemon.MissingNo.nationalPokedexNumber) }
    var imagePath by remember { mutableStateOf("sprites/tile9999.png") }
    var pokemonList by remember { mutableStateOf(listOf(Pokemon.MissingNo.name)) }
    var iconVerticalOffset by remember { mutableStateOf(0) }
    var iconDegrees by remember { mutableStateOf(0f) }
    Row(
        Modifier
            .padding(horizontal = 10.dp)
            .border(
                2.dp,
                color = if (isHover) Color.Black else Color.LightGray,
                shape = RoundedCornerShape(10.dp)
            )
            .pointerMoveFilter(
                onEnter = {
                    isHover = true
                    true
                },
                onExit = {
                    isHover = false
                    false
                }
            )
            .height(rowHeight)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            routeName,
            modifier = Modifier
                .padding(10.dp)
                .weight(0.1f),
            fontSize = TextUnit(12f, TextUnitType.Sp),
        )
        Spacer(modifier = Modifier.align(Alignment.CenterVertically))
        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            isParentHover = isHover,
            heightFraction = 0.8f
        )

        LaunchedEffect(Unit) {
            while (true) {
                delay(100)
                pokemonList = core.getTracking().getValue(routeName)
            }
        }

        LaunchedEffect(Unit) {
            var offset = 5
            while (true) {
                delay(10)
                if (iconVerticalOffset >= 5) offset *= -1
                else if (iconVerticalOffset <= 0) offset *= -1

                iconVerticalOffset += offset
            }
        }

        LaunchedEffect(Unit) {
            var offset = 10
            while (true) {
                delay(10)
                if (iconDegrees >= 5) offset *= -1
                else if (iconDegrees <= -5) offset *= -1

                iconDegrees += offset
            }
        }

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
            pokemonList.forEachIndexed { index, it ->
                pokedexNumber = Pokemon.findByName(it).nationalPokedexNumber - 1
                imagePath = if (pokedexNumber < 10) {
                    "sprites/tile00${pokedexNumber}.png"
                } else if (pokedexNumber < 100) {
                    "sprites/tile0${pokedexNumber}.png"
                } else {
                    "sprites/tile${pokedexNumber}.png"
                }
                Image(
                    painter = painterResource(imagePath),
                    contentDescription = it,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .scale(1f)
                        .offset(
                            (index * -64).dp,
                            0.dp
                        )
                        .rotate(iconDegrees * (if (Pokemon.findByName(it).nationalPokedexNumber % 2 == 0) -1 else 1))
                )
            }
        }
    }
}

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    isParentHover: Boolean,
    heightFraction: Float = 0.8f
) {
    Canvas(modifier = modifier) {
        val paint = Paint().apply {
            this.color = if (isParentHover) Color.Black else Color.LightGray
            this.strokeWidth = 0.5f
        }

        val startX = size.width / 2
        val totalPadding = size.height * (1 - heightFraction)
        val startY = totalPadding / 2
        val endY = size.height - startY

        drawIntoCanvas { canvas ->
            canvas.drawLine(
                p1 = Offset(startX, startY),
                p2 = Offset(startX, endY),
                paint = paint
            )
        }
    }
}

