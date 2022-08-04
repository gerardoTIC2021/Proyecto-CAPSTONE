package mx.tictac.sicutng

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {
    private val GOOGLE_SIGN_IN = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        //Change theme of the app after the splashscreen
        setTheme(R.style.Theme_SICUTNG)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        //Analytics Event
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Complete firebase integration.")
        analytics.logEvent("InitScreen", bundle)

        // Setup
        notification()
        setup()
        session()
    }

    override fun onStart() {
        super.onStart()
        authLayout.visibility = View.VISIBLE
    }
    private fun session(){
        // Saving data
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        if(email != null && provider != null){
            authLayout.visibility = View.INVISIBLE
            showHome(email, ProviderType.valueOf(provider))

        }

    }

    private fun notification(){
//        Log.i("Token", FirebaseMessaging.getInstance().token.result)


    }

    private fun setup() {
        title = "Authentication"
        signUpButton.setOnClickListener {
            if(emailEditText.text.isNotEmpty()&&passEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    emailEditText.text.toString(), passEditText.text.toString()).addOnCompleteListener {
                        if(it.isSuccessful){
                            showHome(it.result?.user?.email ?:"", ProviderType.BASIC)
                        }else{
                            showAlert()
                        }
                }
            }
        }

        loginButton.setOnClickListener {
            if(emailEditText.text.isNotEmpty()&&passEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    emailEditText.text.toString(), passEditText.text.toString()).addOnCompleteListener {

                    if(it.isSuccessful){
                        showHome(it.result?.user?.email ?:"", ProviderType.BASIC)
                    }else{
                        showAlert()
                    }
                }
            }
        }

        googleButton.setOnClickListener {
            //Setup
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("811978808003-9ckilc17trvhinqqrq02caon1mbkotsu.apps.googleusercontent.com")
                .requestEmail()
                .build()
            val googleClient = GoogleSignIn.getClient(this, googleConf)

            googleClient.signOut()
            /* Test
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                onActivityResult(GOOGLE_SIGN_IN, result)
            }.launch(intent)
            */
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)


        }

    }


/*
    private fun onActivityResult(requestCode: Int, result: ActivityResult) {
        if(result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            when (requestCode) {
                GOOGLE_SIGN_IN -> {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(intent)

                    try {
                        val account = task.getResult(ApiException::class.java)
                        if(account!= null){
                            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                                if (it.isSuccessful){
                                    showHome(account.email?:"", ProviderType.GOOGLE)
                                }else{
                                    showAlert()
                                }
                            }
                        }

                    }catch (e: ApiException){
                        showAlert()
                    }
                }
            }
        }
    }
*/

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                if(account!= null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful){
                            showHome(account.email?:"", ProviderType.GOOGLE)
                        }else{
                            showAlert()
                        }
                    }
                }

            }catch (e: ApiException){
                showAlert()
            }
        }
    }




    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("An error has occurred in user authentication!")
        builder.setPositiveButton("OK", null)
        val dialog: AlertDialog=builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType){
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }
}