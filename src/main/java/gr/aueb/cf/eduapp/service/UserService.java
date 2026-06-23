package gr.aueb.cf.eduapp.service;

import gr.aueb.cf.eduapp.core.exception.EntityAlreadyExistsException;
import gr.aueb.cf.eduapp.core.exception.EntityInvalidArgumentException;
import gr.aueb.cf.eduapp.dto.UserInsertDTO;
import gr.aueb.cf.eduapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.eduapp.mapper.Mapper;
import gr.aueb.cf.eduapp.model.Role;
import gr.aueb.cf.eduapp.model.User;
import gr.aueb.cf.eduapp.repository.RoleRepository;
import gr.aueb.cf.eduapp.repository.UserRepository;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j //TODO
@RequiredArgsConstructor

public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final Mapper mapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityAlreadyExistsException.class})
    @Override
    public UserReadOnlyDTO saveUser(UserInsertDTO userInsertDTO) throws EntityAlreadyExistsException, EntityInvalidArgumentException {
       try{

           if(userRepository.findByUsername(userInsertDTO.username()).isPresent()){
               throw new EntityAlreadyExistsException("User", "User with username=" + userInsertDTO.username() +" already exists.");
           }
           User user = mapper.mapToUserEntity(userInsertDTO);
           user.setPassword(passwordEncoder.encode(userInsertDTO.password()));
           Role role = roleRepository.findById(userInsertDTO.roleId())
                   .orElseThrow( () -> new EntityInvalidArgumentException("Role", "Role with id=" + userInsertDTO.roleId() + "does exist"));
           userRepository.save(user);
           log.info("User with username ={} saved successfully", userInsertDTO.username());
           return mapper.mapToUserReadOnlyDTO(user);
       }
       catch(EntityAlreadyExistsException e){
           log.error("Save failed. User with username {} already exists" , userInsertDTO.username());
           throw e;
        }
       catch(EntityInvalidArgumentException e){
           log.error("Save failed. Invalid argument for user with username {} already exists" , userInsertDTO.username());
           throw e;
       }
    }

    @Override
    public UserReadOnlyDTO getUserByUuid(UUID uuid) {
        return null;
    }

    @Override
    public UserReadOnlyDTO getUserByUuidDeletedFalse(UUID uuid) {
        return null;
    }
}
