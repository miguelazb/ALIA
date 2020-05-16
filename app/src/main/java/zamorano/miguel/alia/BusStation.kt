package zamorano.miguel.alia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_bus_station.*

class BusStation : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var camion: String
    lateinit var colonia: String

    var conductoras = ArrayList<User>()

    var database = FirebaseDatabase.getInstance().reference
    lateinit var usuarioUID: String

    var listaRutas = ArrayList<ListaRutasConductor>()
    lateinit var adaptador: RutasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_station)
        // Obtiene el dato de usuario
        obtenDatosUsuario()

        // Carga la lista de prueba
        llenaSpinnerColonias()
        llenaSpinnerRutas()

        // Si se clickea el boton Menu Perfil
        btnMenuPerfil.setOnClickListener {
            val intent: Intent = Intent(this, MiPerfilActivity::class.java)
            startActivity(intent)
        }
        btn_siguiente.setOnClickListener{
            if(validaDatos()) {
                val intent: Intent = Intent(this, EligeConductorActivity::class.java)
                intent.putExtra("camion", camion)
                intent.putExtra("colonia", colonia)
                startActivity(intent)
            }
        }

    }


    fun validaDatos(): Boolean {
        if (camion.isEmpty()) {
            showAlert("Seleccione una línea de camión primero.")
            return false
        } else if (colonia.isEmpty()) {
            showAlert("Seleccione una colonia primero.")
            return false
        }
        return true
    }

    /**
     * Método ShowAlert construirá y mostrará un error en caso que existan
     * campos vacíos.
     */
    private fun showAlert(msg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(msg)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * Método obtenDatoUsuario nos da el nombre del usuario existente
     * en la base de datos Firebase.
     */
    fun obtenDatosUsuario() {
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        usuarioUID = currentFirebaseUser!!.uid

        database.child("users")
            .child(usuarioUID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var map = p0.value as Map<*, *>
                    textViewNombre.text = "Bienvenida, ${map["nombre"].toString()}"
                }

            })
    }




    /**
     * El método llenaSpinnerColonias crea un ArrayAdapter usando el arreglo de Strings
     * en el drawable de Strings para crear el Spinner con las diferentes opciones.
     */
    private fun llenaSpinnerColonias() {
        val spinner: Spinner = findViewById(R.id.spinnerColoniasPA)

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.colonia_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
    }

    /**
     * El método llenaSpinnerRutas crea un ArrayAdapter usando el arreglo de Strings
     * en el drawable de Strings para crear el Spinner con las diferentes opciones.
     */
    private fun llenaSpinnerRutas() {
        val spinner: Spinner = findViewById(R.id.spinnerLineasPA)

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.bus_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
    }

    /**
     * Método onItemSelected iguala la variable lineaCamion al valor seleccionado
     * en el Spinner del activity.
     * @param parent parent del spinner.
     * @param view vista del spinner.
     * @param position posición del elemento seleccionado
     * @param id id del spinner.
     */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spinnerColonias -> {
                if (position != 0) {
                    colonia = parent?.getItemAtPosition(position).toString()
                } else {
                    colonia = ""
                }
            }
            R.id.spinnerLineas -> {
                if (position != 0) {
                    camion = parent?.getItemAtPosition(position).toString()
                } else {
                    camion = ""
                }

            }

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}
