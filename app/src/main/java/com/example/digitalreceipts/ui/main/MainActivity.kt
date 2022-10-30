package com.example.digitalreceipts.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.digitalreceipts.R
import com.example.digitalreceipts.databinding.ActivityMainBinding

/**
 * Activity principal responsável por inicializar o projeto com o Android Jetpack Navigation
 * e com os containers que "carregarão" os fragmentos.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ligando o layout com o binding para acesso dos componentes.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Define a ToolBar como "SupportActionBar" da activity.
        setSupportActionBar(binding.toolbar)

        // Remove sombreamento da AppBar.
        binding.appbar.outlineProvider = null

        // Configuração da navegação da AppBar no fragmento hospedeiro.
        // TODO entender melhor as configurações da ToolBar
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Configuração e definição dos ícones da AppBar.
        // TODO entender melhor as configurações da ToolBar
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}