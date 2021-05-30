package com.example.springdatasecurity.services;

import com.example.springdatasecurity.entities.Role;
import com.example.springdatasecurity.entities.User;
import com.example.springdatasecurity.repositories.RoleRepository;
import com.example.springdatasecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.stream.Collectors;
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public User findByUserName(String username) {
        return userRepository.findOneByUserName(username);
    }

    // КАК СПРИНГ ПОЛУЧАЕТ ПОЛЬЗОВАТЕЛЕЙ?
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findOneByUserName(userName);
        if (user==null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        //достали user'а и по нему формируем user details
        //для формирования нужно имя, пароль, список ролей
        return  new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    // список ролей
    // преобразуем роли пользователя в роли спринга
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role-> new SimpleGrantedAuthority(role.getName()))
                .collect((Collectors.toList()));
    }


}
