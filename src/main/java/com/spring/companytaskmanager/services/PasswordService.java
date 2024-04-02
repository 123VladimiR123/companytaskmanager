package com.spring.companytaskmanager.services;

import com.spring.companytaskmanager.DTO.UserDTO;
import com.spring.companytaskmanager.entities.ChangeEmailEntity;
import com.spring.companytaskmanager.entities.UserInfoEntity;
import com.spring.companytaskmanager.enums.Roles;
import com.spring.companytaskmanager.reps.DepartmentRepository;
import com.spring.companytaskmanager.reps.EmailChangeRepository;
import com.spring.companytaskmanager.reps.UserInfoRepository;
import jakarta.persistence.Temporal;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@AllArgsConstructor
@Service
public class PasswordService {
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;
    private final EmailChangeRepository emailChangeRepository;
    private final UserInfoRepository userInfoRepository;
    //WTF am I doing?
    private final DataSource dataSource;
    private final Logger logger;

    @Transactional
    public void changeEmail(UserInfoEntity userInfoEntity, String newEmail) throws IllegalArgumentException{
        if (userDetailsManager.userExists(newEmail)) throw new IllegalArgumentException("Такая почта уже зарегестрирована!");
        //ToDo Скинуть ключ на новую(?) почту
        UUID key = UUID.randomUUID();
        logger.info("Your code is {}", key);
        ChangeEmailEntity emailEntity = ChangeEmailEntity.builder()
                .newEmail(newEmail)
                .oldEmail(userInfoEntity.getUsername())
                .expiredTime(Timestamp.valueOf(LocalDateTime.now().plusMinutes(20)))
                .key(key.toString())
                .build();
        emailChangeRepository.save(emailEntity);
    }

    @Transactional
    public void verifyEmail(UserInfoEntity userInfoEntity, String key) throws UsernameNotFoundException {

        ChangeEmailEntity emailEntity = emailChangeRepository
                .findChangeEmailEntityByExpiredTimeBeforeAndOldEmailAndKey(Timestamp.valueOf(LocalDateTime.now()),
                        userInfoEntity.getUsername(), key);

        if (emailEntity == null) throw new UsernameNotFoundException("Неправильный ключ");

        try (Connection connection = dataSource.getConnection()){

            PreparedStatement statement = connection.prepareStatement("UPDATE users SET username = " +
                        "\'" + emailEntity.getNewEmail() + "\'" + " WHERE username = " +
                        "\'" + emailEntity.getOldEmail() + "\'");
            statement.executeUpdate();

        } catch (SQLException e) {throw new UsernameNotFoundException("Ошибка на стороне базы данных");}
    }
    @Transactional
    public void recoverPassword(UserInfoEntity userInfoEntity) {
        //ToDo Скинуть на почту пароль
        UUID newPassword = UUID.randomUUID();
        logger.info("New password is {}", newPassword);
        UserDetails recovered = User.builder()
                .username(userInfoEntity.getUsername())
                .password(passwordEncoder.encode(newPassword.toString()))
                .authorities(userDetailsManager.loadUserByUsername(userInfoEntity.getUsername()).getAuthorities())
                .build();
        userDetailsManager.updateUser(recovered);
    }

    @Transactional
    public void changePassword(UserInfoEntity userInfoEntity, String newPassword,
                               String newPassword2, String oldPassword) throws IllegalArgumentException {
        // ToDo проверить старый пароль
        if (!newPassword2.equals(newPassword)) throw new IllegalArgumentException();
        UserDetails newUserPassword = User.builder()
                .password(passwordEncoder.encode(newPassword))
                .username(userInfoEntity.getUsername())
                .authorities(userDetailsManager.loadUserByUsername(userInfoEntity.getUsername()).getAuthorities())
                .build();
        userDetailsManager.updateUser(newUserPassword);
    }

    @Transactional
    public void disableUser(UserInfoEntity userInfo) {
        UserDetails user = userDetailsManager.loadUserByUsername(userInfo.getUsername());
        if (!user.isAccountNonLocked()) recoverPassword(userInfo);
        UserDetails newUser = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(user.isAccountNonLocked())
                .authorities(user.getAuthorities())
                .build();
        userDetailsManager.updateUser(newUser);
    }

    @Transactional
    public void createUser(UserDTO userDTO) {
        UserDetails userDetails = User.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .authorities((userDTO.getIsAdmin() != null) ? new String[]{Roles.ADMIN.getRole(), Roles.USER.getRole()} : new String[]{Roles.USER.getRole()})
                .disabled(false)
                .build();

        if (userDetailsManager.userExists(userDTO.getUsername())) userDetailsManager.updateUser(userDetails);
        else userDetailsManager.createUser(userDetails);

        UserInfoEntity user = UserInfoEntity.builder()
                .departmentName(departmentRepository.findById(userDTO.getDepartmentName()).orElse(null))
                .birthDate(Date.valueOf(userDTO.getBirthDate()))
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .fatherName((userDTO.getFatherName().isEmpty()) ? null : userDTO.getFatherName())
                .phoneNumber("+7" + userDTO.getPhoneNumber())
                .username(userDTO.getUsername())
                .role(userDTO.getRole())
                .build();

        userInfoRepository.save(user);
    }
}