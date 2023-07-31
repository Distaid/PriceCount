package aid.distaid.pricecount.navigation

sealed class NavigationItem(val route: String, val path: String) {
    object Home: NavigationItem("home", "home")
    object CreateProduct: NavigationItem("createProduct", "createProduct")
    object EditProduct: NavigationItem("editProduct/{id}", "editProduct/")
}