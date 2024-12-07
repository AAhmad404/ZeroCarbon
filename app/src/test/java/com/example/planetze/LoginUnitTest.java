package com.example.planetze;

import static android.app.Activity.RESULT_OK;
import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import static org.mockito.Mockito.doThrow;


import static org.junit.Assert.*;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.planetze.Login.LoginModel;
import com.example.planetze.Login.LoginPresenter;
import com.example.planetze.Login.LoginView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class LoginUnitTest {
    @Mock LoginModel model;
    @Mock LoginView view;


    @Test
    public void testNullEmail() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser(null, "password123");
        verify(view).setMessage("Email cannot be empty");
        when(model.isLoggedIn()).thenReturn(false);

    }

    @Test
    public void testNullPassword() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser("jiangminki0@gmail.com", null);
        verify(view).setMessage("Password cannot be empty");
        when(model.isLoggedIn()).thenReturn(false);

    }

    @Test
    public void testEmptyEmail() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser("", "password123");
        verify(view).setMessage("Email cannot be empty");
        when(model.isLoggedIn()).thenReturn(false);

    }

    @Test
    public void testEmptyPassword() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser("jiangminki0@gmail.com", "");
        verify(view).setMessage("Password cannot be empty");
        when(model.isLoggedIn()).thenReturn(false);
    }

    @Test
    public void testEmptyEmailWithSpace() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser("     ", "password123");
        verify(view).setMessage("Email cannot be empty");
        when(model.isLoggedIn()).thenReturn(false);

    }

    @Test
    public void testEmptyPasswordWithSpace() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser("jiangminki0@gmail.com", "       ");
        verify(view).setMessage("Password cannot be empty");
        when(model.isLoggedIn()).thenReturn(false);
    }

    @Test
    public void testWrongEmailRightPassword() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser("wrongemail@gmail.com", "hellohello1");
        when(model.isLoggedIn()).thenReturn(false);
    }

    @Test
    public void testRightEmailWrongPassword() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser("jiangminki0@gmail.com", "wrongpassword");
        when(model.isLoggedIn()).thenReturn(false);
    }

    @Test
    public void testWrongEmailWrongPassword() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser("wrongemail@gmail.com", "wrongpassword");
        when(model.isLoggedIn()).thenReturn(false);
    }

    @Test
    public void testRightEmailRightPassword() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser("jiangminki0@gmail.com", "hellohello1");
        when(model.isLoggedIn()).thenReturn(true);
    }

    @Test
    public void testMessage() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.setMessage("this is a message");
        verify(view).setMessage("this is a message");

    }

    @Test
    public void testTakeToSurvery() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.takeToSurvey();
        verify(view).takeToSurvey();
    }

    @Test
    public void testTakeToHub() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.takeToHub();
        verify(view).takeToHub();
    }

    @Test
    public void testGetViewContext() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        Context context = view.getViewContext();
        when(presenter.getViewContext()).thenReturn(context);
    }

    @Test
    public void testSignUpLauncher() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.setSignUpLauncher();
        verify(view).setSignUpLauncher();
    }

    @Test
    public void testSignInResult() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        ActivityResult result = null;
        presenter.onSignInResult(result);
        when(model.isLoggedIn()).thenReturn(true);
    }

    @Test
    public void testStartGoogleSignIn() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.startGoogleSignin();
        verify(view).startGoogleSignin();
    }

}
