import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// For Swift interop
fun <T> Flow<T>.asCFlow(): Flow<T> = this

fun <T> StateFlow<T>.subscribe(
    scope: CoroutineScope = MainScope(),
    onEach: (T) -> Unit
) {
    scope.launch {
        this@subscribe.collect {
            onEach(it)
        }
    }
}