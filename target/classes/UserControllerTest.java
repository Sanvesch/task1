
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userRepository);
    }

    @Test
    public void testRegisterUserSuccess() {
        User userToRegister = createUser();
        when(userRepository.save(any(User.class))).thenReturn(userToRegister);

        ResponseEntity<?> response = userController.registerUser(userToRegister);

        verify(userRepository, times(1)).save(userToRegister);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Utilisateur enregistré avec succès.", response.getBody());
    }

    @Test
    public void testRegisterUserUserExists() {
        User userToRegister = createUser();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userToRegister));

        ResponseEntity<?> response = userController.registerUser(userToRegister);

        verify(userRepository, never()).save(any(User.class));
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("L'utilisateur existe déjà.", response.getBody());
    }

    @Test
    public void testRegisterUserUnderage() {
        User underageUser = createUser();
        underageUser.setBirthdate(LocalDate.now().minusYears(17)); // Moins de 18 ans

        ResponseEntity<?> response = userController.registerUser(underageUser);

        verify(userRepository, never()).save(any(User.class));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("L'utilisateur doit être majeur.", response.getBody());
    }

    // Ajoutez d'autres tests selon vos besoins pour la méthode registerUser

    @Test
    public void testGetUserDetailsSuccess() {
        String username = "testuser";
        User user = createUser();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userController.getUserDetails(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testGetUserDetailsUserNotFound() {
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.getUserDetails(username);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Utilisateur non trouvé.", response.getBody());
    }

    // Ajoutez d'autres tests selon vos besoins pour la méthode getUserDetails

    private User createUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setBirthdate(LocalDate.now().minusYears(20)); // 20 ans ou plus
        user.setCountry("France");
        return user;
    }
}
