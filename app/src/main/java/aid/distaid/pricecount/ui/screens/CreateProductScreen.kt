package aid.distaid.pricecount.ui.screens

import aid.distaid.pricecount.R
import aid.distaid.pricecount.data.models.createEmptyProduct
import aid.distaid.pricecount.data.sql.AidDbHandler
import aid.distaid.pricecount.format
import aid.distaid.pricecount.ui.AidBackTopAppBar
import aid.distaid.pricecount.ui.SelectDialog
import android.graphics.ImageDecoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateItemScreen(
    onBack: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val dbHandler = AidDbHandler(context)

    var newProduct by remember {
        mutableStateOf(createEmptyProduct())
    }

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    val scrollState = rememberScrollState(0)

    LaunchedEffect(scrollState.maxValue) {
         scrollState.animateScrollTo(scrollState.maxValue)
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        val time = System.currentTimeMillis()

        newProduct = if (uri != null) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            newProduct.copy(
                image = "${uri.lastPathSegment!!.split(":")[1]}_$time",
                imageBitmap = ImageDecoder.decodeBitmap(source)
            )
        } else {
            newProduct.copy(
                image = null,
                imageBitmap = null
            )
        }
    }

    fun roundPrice() {
        newProduct = if (newProduct.price == "") {
            newProduct.copy(price = "0.00")
        } else {
            newProduct.copy(price = "%.2f".format(newProduct.price.toFloat()).replace(',', '.'))
        }
    }

    fun calculateSum() {
        val count = if (newProduct.count == "") 0 else newProduct.count.toInt()
        val price = if (newProduct.price == "" || newProduct.price == ".") 0f else newProduct.price.toFloat()
        newProduct = newProduct.copy(sum = count * price)
    }

    fun saveItem() {
        dbHandler.productsHandler.add(newProduct)
        onBack()
    }

    Scaffold(
        topBar = {
            AidBackTopAppBar(
                titleFromResource = R.string.productCreate,
                onBack = onBack
            )
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                })
            }
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(onClick = {
                        launcher.launch("image/*")
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.image_24),
                            contentDescription = "add",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                        Text(
                            text = stringResource(id = R.string.choosePhoto).uppercase(),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    newProduct.imageBitmap?.let { image ->
                        Image(
                            bitmap = image.asImageBitmap(),
                            contentDescription = "userImage",
                            modifier = Modifier.size(170.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
                Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = newProduct.name,
                    singleLine = true,
                    label = { Text(text = stringResource(id = R.string.productName)) },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    onValueChange = { value -> newProduct = newProduct.copy(name = value) }
                )
                Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { dialogOpen = true },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface
                    ),
                    value = newProduct.category?.name ?: "",
                    singleLine = true,
                    label = { Text(text = stringResource(id = R.string.productCategory)) },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    onValueChange = {  },
                    enabled = false,
                    trailingIcon = {
                        if (newProduct.category != null) {
                            IconButton(onClick = {
                                newProduct = newProduct.copy(
                                    category = null,
                                    categoryId = null
                                )
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.clear_24),
                                    contentDescription = "clear"
                                )
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    value = newProduct.link ?: "",
                    singleLine = true,
                    label = { Text(text = stringResource(id = R.string.productLink)) },
                    onValueChange = { value -> newProduct = newProduct.copy(link = value) }
                )
                Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .onFocusChanged { state ->
                                if (!state.hasFocus && newProduct.count == "") {
                                    newProduct = newProduct.copy(count = "1")
                                    calculateSum()
                                }
                            },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = newProduct.count,
                        singleLine = true,
                        label = { Text(text = stringResource(id = R.string.productCount)) },
                        onValueChange = { value ->
                            if (value.contains(" ") || value.contains(",") || value.contains(".") || value.contains("-"))
                                return@OutlinedTextField
                            if (value != "" && value.toInt() > 999) {
                                return@OutlinedTextField
                            }
                            newProduct = newProduct.copy(count = value)
                            calculateSum()
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .onFocusChanged { state ->
                                if (!state.hasFocus) {
                                    roundPrice()
                                    calculateSum()
                                }
                            },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        keyboardActions = KeyboardActions(onAny = { roundPrice() }),
                        value = newProduct.price,
                        singleLine = true,
                        label = { Text(text = stringResource(id = R.string.productPrice)) },
                        onValueChange = { value ->
                            if (value.contains(" ") || value.contains("-"))
                                return@OutlinedTextField
                            if (value.contains(".")) {
                                val parts = value.split('.')
                                if (parts[0].length > 1 && parts[0].toInt() > 99999)
                                    return@OutlinedTextField
                                if (parts[1].length > 2)
                                    return@OutlinedTextField
                            } else {
                                if (value.length > 1 && value.toInt() > 99999)
                                    return@OutlinedTextField
                            }
                            newProduct = newProduct.copy(price = value.replace(',', '.'))
                            calculateSum()
                        }
                    )
                }
                Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
                Text(
                    text = stringResource(id = R.string.productSum, newProduct.sum.format(2)).uppercase(),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(id = R.string.productDescription)) },
                    value = newProduct.description ?: "",
                    minLines = 3,
                    maxLines = 10,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    onValueChange = { value -> newProduct = newProduct.copy(description = value)}
                )
                Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    OutlinedButton(
                        onClick = { saveItem() }
                    ) {
                        Text(text = stringResource(id = R.string.save).uppercase())
                    }
                    OutlinedButton(
                        onClick = onBack,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                    ) {
                        Text(text = stringResource(id = R.string.cancel).uppercase())
                    }
                }
                SelectDialog(
                    open = dialogOpen,
                    dialogLabel = stringResource(id = R.string.chooseCategory),
                    items = dbHandler.categoriesHandler.getAll(),
                    onConfirm = { category ->
                        newProduct = newProduct.copy(
                            category = category,
                            categoryId = category.id
                        )
                    },
                    onClose = { dialogOpen = false },
                    onSelectedItem = { category ->
                        if (newProduct.category == null) {
                            false
                        }
                        else {
                            newProduct.category!!.id == category.id
                        }
                    }
                )
            }
        }
    }
}