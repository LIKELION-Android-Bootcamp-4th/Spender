import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class BottomNavigationShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val width = size.width
        val height = size.height
        val radius = with(density) { 36.dp.toPx() }

        val center = width / 2
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(center - radius - 16f, 0f)

            // 곡선 파임 시작
            cubicTo(
                center - radius, 0f,
                center - radius, radius,
                center, radius + 8f
            )
            // 곡선 파임 끝
            cubicTo(
                center + radius, radius,
                center + radius, 0f,
                center + radius + 16f, 0f
            )

            lineTo(width, 0f)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }

        return Outline.Generic(path)
    }
}