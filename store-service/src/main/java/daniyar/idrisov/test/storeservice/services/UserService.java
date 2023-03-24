package daniyar.idrisov.test.storeservice.services;

import daniyar.idrisov.test.storeservice.exceptions.UpdateYourselfStateException;
import daniyar.idrisov.test.storeservice.exceptions.UserAlreadyStateException;
import daniyar.idrisov.test.storeservice.exceptions.UserNotFoundException;
import daniyar.idrisov.test.storeservice.models.dto.UserDTO;
import daniyar.idrisov.test.storeservice.models.dto.UserWithChildsDTO;
import daniyar.idrisov.test.storeservice.models.enumerated.Role;
import daniyar.idrisov.test.storeservice.models.enumerated.UserState;
import daniyar.idrisov.test.storeservice.models.jpa.User;
import daniyar.idrisov.test.storeservice.models.mappers.UserMapper;
import daniyar.idrisov.test.storeservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public List<UserWithChildsDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserWithChildsDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserWithChildsDTO getCurrentUserDTO() {
        return userMapper.toUserWithChildsDTO(getCurrentUser());
    }

    @Transactional
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(Role.ADMIN.name());
        return authentication.getAuthorities().contains(adminAuthority);
    }

    @Transactional
    public UserWithChildsDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return userMapper.toUserWithChildsDTO(user);
    }

    @Transactional
    public UserDTO activateUser(Long userId) {
        User updateUser = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        checkForEditYourselfState(updateUser);
        checkForAlreadyState(updateUser.getState(), UserState.ACTIVE);
        updateUser.setState(UserState.ACTIVE);
        User user = userRepository.save(updateUser);
        return userMapper.toUserDto(user);
    }

    private void checkForEditYourselfState(User updateUser) {
        User currentUser = getCurrentUser();
        if (currentUser.getId().equals(updateUser.getId())) {
            throw new UpdateYourselfStateException();
        }
    }

    @Transactional
    public UserDTO freezeUser(Long userId) {
        User updateUser = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        checkForAlreadyState(updateUser.getState(), UserState.FROZEN);
        updateUser.setState(UserState.FROZEN);
        User user = userRepository.save(updateUser);
        return userMapper.toUserDto(user);
    }

    @Transactional
    public UserDTO banUser(Long userId) {
        User updateUser = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        checkForAlreadyState(updateUser.getState(), UserState.BANNED);
        updateUser.setState(UserState.BANNED);
        User user = userRepository.save(updateUser);
        return userMapper.toUserDto(user);
    }

    private void checkForAlreadyState(UserState currentState, UserState state) {
        if (currentState.equals(state)) {
            throw new UserAlreadyStateException();
        }
    }

    @Transactional
    public UserDTO replenishBalance(Long userId, Double sum) {
        User updateUser = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Double balance = updateUser.getBalance();
        updateUser.setBalance(balance + sum);
        User user = userRepository.save(updateUser);
        return userMapper.toUserDto(user);
    }

}
