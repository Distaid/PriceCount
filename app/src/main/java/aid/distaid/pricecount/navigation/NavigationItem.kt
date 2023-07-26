package aid.distaid.pricecount.navigation

sealed class NavigationItem(val route: String) {
    object Home: NavigationItem("home")
    object CreateProduct: NavigationItem("createProduct")
    object ShowProduct: NavigationItem("showProduct/{id}")
    object EditProduct: NavigationItem("editProduct/{id}")
}