package com.example.eyeOnTheNews.ui.authentication

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.autotranstandaloneinspection.R
import com.example.eyeOnTheNews.ui.theme.EyeOnTheNewsTheme

@Composable
fun loginScreen(viewModel: AuthViewModel = hiltViewModel()) {
    Scaffold(contentWindowInsets = WindowInsets(15.dp, 15.dp, 15.dp, 15.dp)) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.consumeWindowInsets(it)
        ) {
            header()
            signIn()
            rememberMe()
            loginButton()
            signUP()
            divider()
            signInWithGoogle()
            signInWithFacebook()

            val uiState = viewModel.uiState.collectAsState()

            if (uiState.value.registerResult == "success") {
                var context: Context = getApplicationContext()
                Toast.makeText(
                    context,
                    "Registration was Successful. Check your email for a confirmation. You may use the link to login or login here",
                    Toast.LENGTH_SHORT
                ).show()

            }

            if (uiState.value.registerResult == "error") {
                var context: Context = getApplicationContext()
                Toast.makeText(context, "Registration was unsuccessful", Toast.LENGTH_SHORT).show()
            }

            if (uiState.value.loginResult == "success") {
                var context: Context = getApplicationContext()
                Toast.makeText(context, " Login successful", Toast.LENGTH_SHORT).show()
            }

            if (uiState.value.loginResult == "error") {
                var context: Context = getApplicationContext()
                Toast.makeText(
                    context,
                    "Incorrect username or password. Try again.",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }
}

@Composable
fun header() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.inspect_it),
            contentDescription = "Inspect it logo",
            modifier = Modifier.size(200.dp)
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 5.dp, 5.dp, 5.dp)
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 5.dp, 5.dp, 5.dp)
    ) {
        Text(
            text = "How do I get started with an Inspection?",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.W300
        )
    }
}

@Composable
fun signIn() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 5.dp, 5.dp, 5.dp)
    ) {
        Text(text = "Email", style = MaterialTheme.typography.labelLarge)
        Text(
            text = "*",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("lanyerod@gmail.com") },
            singleLine = true,
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .padding(5.dp, 0.dp, 5.dp, 0.dp)
                .fillMaxWidth()
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 5.dp, 5.dp, 5.dp)
    ) {
        Text(text = "Password", style = MaterialTheme.typography.labelLarge)
        Text(
            text = "*",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            visualTransformation = VisualTransformation.None,
            trailingIcon = {
                Icon(
                    Icons.Rounded.Lock,
                    contentDescription = "hide or view password"
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(25.dp),

            modifier = Modifier
                .padding(5.dp, 0.dp, 5.dp, 0.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun signUP() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {

        OutlinedButton(
            onClick = { /*TODO*/ },
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .padding(5.dp, 5.dp, 5.dp, 5.dp)
                .fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}

@Composable
fun rememberMe() {
    //val (checkedState, onStateChange) = remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 10.dp, 5.dp, 5.dp),
        /*.toggleable(
            value = checkedState,
            onValueChange = { onStateChange(!checkedState) },
            role = Role.Checkbox
        )*/

    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            Row {
                Checkbox(
                    checked = true,
                    onCheckedChange = null // null recommended for accessibility with screenreaders
                )
                Text(
                    text = "Remember me",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Forgot Password?",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }


}

@Composable
fun loginButton() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = {},
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            enabled = true,
            colors = ButtonDefaults.buttonColors(),
            modifier = Modifier
                .padding(5.dp, 15.dp, 5.dp, 15.dp)
                .fillMaxWidth()
        )
        {
            Text(text = "Login")
        }
    }
}

@Composable
fun divider() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 15.dp, 5.dp, 15.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 5.dp)
                .weight(1f)
        ) {
            HorizontalDivider()
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(2f)
        ) {
            Text(
                text = "Or Sign in with Google",
                fontWeight = FontWeight.W400,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .padding(5.dp, 0.dp, 0.dp, 0.dp)
                .weight(1f)
        ) {
            HorizontalDivider()
        }
    }
}

@Composable
fun signInWithGoogle() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {

        OutlinedButton(
            onClick = { /*TODO*/ },
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            enabled = true,
            modifier = Modifier
                .padding(5.dp, 15.dp, 5.dp, 15.dp)
                .fillMaxWidth()
        )
        {
            Image(
                painter = painterResource(id = R.drawable.google_color_icon),
                contentDescription = "Google Icon",
                modifier = Modifier
                    .padding(0.dp, 0.dp, 15.dp, 0.dp)
                    .size(20.dp)
            )
            Text(text = "Sign in with Google", color = MaterialTheme.colorScheme.onBackground)
        }
    }
}

@Composable
fun signInWithFacebook() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {

        OutlinedButton(
            onClick = { /*TODO*/ },
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            enabled = true,
            modifier = Modifier
                .padding(5.dp, 5.dp, 5.dp, 5.dp)
                .fillMaxWidth()
        )
        {
            Image(
                painter = painterResource(id = R.drawable.facebook_svgrepo_com_2),
                contentDescription = "facebook Icon",
                modifier = Modifier
                    .padding(0.dp, 0.dp, 15.dp, 0.dp)
                    .size(28.dp)
            )
            Text(text = "Sign in with Facebook", color = MaterialTheme.colorScheme.onBackground)
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun loginScreen() {
    EyeOnTheNewsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val scrollState = rememberScrollState()
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                //loginPage()
            }
        }
    }
}
