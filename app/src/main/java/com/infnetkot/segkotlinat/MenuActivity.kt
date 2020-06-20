package com.infnetkot.segkotlinat

import android.os.Bundle
import android.view.Menu
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.infnetkot.segkotlinat.util.AnotacaoStorage

class MenuActivity : AppCompatActivity()/*,
                     BillingClientStateListener,
                     SkuDetailsResponseListener,
                     PurchasesUpdatedListener ,
                     ConsumeResponseListener*/ {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var  anotacaoStorage: AnotacaoStorage

    /*private lateinit var clienteInApp : BillingClient*/
    /*private lateinit var compraViewModel : CompraViewModel*/
    /*private var mapSku = HashMap<String , SkuDetails>()*/
    /*private var currentSku = "android.test.purchased"*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        anotacaoStorage = AnotacaoStorage()

        // compraViewModel = CompraViewModel()

        // Seguindo a sugestão do Google, seria necessária a consulta no banco para ver se o usuário já comprou e atualizar o viewmodel
        // Mas poderíamos usar o método queryPurchases() de BillingClient, para descobrir se foi feita a compra sem usar o banco
        // Como está sendo feito um caso de teste, sempre que for executado o app deverá ser comprado para sumir as propagandas

        /*clienteInApp = BillingClient.newBuilder(this)
            .enablePendingPurchases()
            .setListener(this)
            .build()
        clienteInApp.startConnection(this)*/

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_cadastro, R.id.nav_detalhe, R.id.nav_conta
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /* override fun onDestroy () {
        clienteInApp .endConnection()
        super .onDestroy()
    } */

    /* override fun onBillingSetupFinished (billingResult: BillingResult?) {
        if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK){
            val skuList = arrayListOf ( currentSku )
            val params = SkuDetailsParams.newBuilder()
            params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
            clienteInApp .querySkuDetailsAsync(params.build() , this)
        }
    }

        override fun onBillingServiceDisconnected () {
        }
    */

    /* override fun onSkuDetailsResponse(billingResult: BillingResult?, skuDetailsList: MutableList<SkuDetails>?) {
            if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK){
                mapSku.clear()
                skuDetailsList?.forEach{
                    t ->
                    val preco = t.price
                    val descricao = t.description
                    mapSku[t.sku] = t
                }
       } */

    /* Método chamado no evento onClick do botão de compra
        fun processGoogleInApp(view: View){
            val skuDetails = mapSku[currentSku]
            val purchaseParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build()
            clienteInApp.launchBillingFlow(this, purchaseParams)
        } */

    /* override fun onPurchasesUpdated (
        billingResult: BillingResult ,
        purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                GlobalScope.launch(Dispatchers.IO){
                    handlePurchase(purchase)
                }
            }
        } else if (billingResult. responseCode ==
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            Log.d( "COMPRA>>" , "Produto já foi comprado" )
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.d( "COMPRA>>" , "Usuário cancelou a compra")
        } else {
            Log.d( "COMPRA>>", "Código de erro desconhecido: ${billingResult.responseCode}")
        }
    }
    */

    /*suspend fun handlePurchase (purchase: Purchase) {
        if (purchase. purchaseState === Purchase. PurchaseState . PURCHASED )
        {
            // Guardando no viewmodel a informação de compra serve apenas para testes e verificação em outros fragments ou activities
            // É recomendado pelo Google que seja guardada essa informação em um servidor seguro
            // Sem a persistência da informação em um banco, em toda a execução do app o recurso deveria ser comprado
            // Já que foi usado um produto de teste também.
            // O produto que está sendo comprado será cobrado apenas uma vez, então não será implementado o consumo

            compraViewModel.ModificaStatusParaAdquirido()

            Log.d( "COMPRA>>" , "Produto obtido com sucesso" )

            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams
                    .newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                val ackPurchaseResult = withContext(Dispatchers.IO) {
                    clienteInApp.acknowledgePurchase(
                        acknowledgePurchaseParams.build())
                }
            }
        }
    }
    */


}
