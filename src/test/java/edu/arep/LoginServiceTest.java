package edu.arep;
import edu.arep.login.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class LoginServiceTest {
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        loginService = new LoginService();
        loginService.initializeUsers();
    }

    @Test
    void testLoginWithValidCredentials() {
        assertTrue(loginService.login("usuario1", "password1"));
    }

    @Test
    void testLoginWithInvalidUsername() {
        assertFalse(loginService.login("usuario_invalido", "password"));
    }

    @Test
    void testLoginWithInvalidPassword() {
        assertFalse(loginService.login("usuario1", "contraseña_invalida"));
    }

    @Test
    void testSesionValidaWithValidToken() {
        String token = "token_valido";
        loginService.sesiones.put(token, true);
        assertTrue(loginService.sesionValida(token));
    }

    @Test
    void testSesionValidaWithInvalidToken() {
        assertFalse(loginService.sesionValida("token_invalido"));
    }
}
