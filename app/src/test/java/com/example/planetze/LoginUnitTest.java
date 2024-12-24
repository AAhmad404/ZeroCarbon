package com.example.planetze;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.junit.MockitoJUnitRunner;


import android.content.Context;

import com.example.planetze.login.LoginModel;
import com.example.planetze.login.LoginPresenter;
import com.example.planetze.login.LoginView;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class LoginUnitTest {
    @Mock
    LoginModel model;
    @Mock
    LoginView view;
    @Mock
    Context context;

    @Test
    public void testNullEmail() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.loginUser(null, "password123");

        verify(view).setMessage("Email cannot be empty");
    }

    @Test
    public void testEmptyEmail() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.loginUser("", "password123");

        verify(view).setMessage("Email cannot be empty");
    }

    @Test
    public void testEmptyEmailWithSpace() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.loginUser("     ", "password123");

        verify(view).setMessage("Email cannot be empty");
    }

    @Test
    public void testNullPassword() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.loginUser("test@gmail.com", null);

        verify(view).setMessage("Password cannot be empty");
    }

    @Test
    public void testEmptyPassword() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.loginUser("test@gmail.com", "");

        verify(view).setMessage("Password cannot be empty");
    }

    @Test
    public void testEmptyPasswordWithSpace() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.loginUser("jiangminki0@gmail.com", "  ");

        verify(view).setMessage("Password cannot be empty");
    }

    @Test
    public void testValidCredentials() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.loginUser("Email@gmail.com", "password");

        verify(model).loginUser("Email@gmail.com", "password", presenter);
    }

    @Test
    public void testMessage() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.setMessage("message");

        verify(view).setMessage("message");
    }

    @Test
    public void testTakeToSurvey() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.takeToSurvey();

        verify(view).takeToSurvey();
    }

    @Test
    public void testTakeToHub() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.takeToHub();

        verify(view).takeToHub();
    }

    @Test
    public void testGetViewContext() {
        when(view.getContext()).thenReturn(context);
        LoginPresenter presenter = new LoginPresenter(model, view);

        assertEquals(presenter.getViewContext(), context);
    }

    @Test
    public void testSignUpLauncher() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.setSignUpLauncher();

        verify(view).setSignUpLauncher();
    }

    @Test
    public void testSignInResult() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.onSignInResult(null);

        verify(model).onSignInResult(null, presenter);
    }

    @Test
    public void testStartGoogleSignIn() {
        LoginPresenter presenter = new LoginPresenter(model, view);
        presenter.startGoogleSignIn();

        verify(view).startGoogleSignIn();
    }
}
