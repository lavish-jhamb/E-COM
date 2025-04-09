package com.ecomhub.user.service.service.customer;

import com.ecomhub.user.service.dto.AddressDTO;
import com.ecomhub.user.service.entity.Address;
import com.ecomhub.user.service.entity.Profile;
import com.ecomhub.user.service.exception.AddressNotFoundException;
import com.ecomhub.user.service.exception.AddressLimitExceededException;
import com.ecomhub.user.service.exception.ProfileNotFoundException;
import com.ecomhub.user.service.repository.AddressRepository;
import com.ecomhub.user.service.repository.ProfileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ModelMapper mapper;

    public List<AddressDTO> getAddress(int accountId) {
        return addressRepository.findByProfile_Account_Id(accountId)
                .stream().map(address -> mapper.map(address, AddressDTO.class)).toList();
    }

    public AddressDTO createAddress(int accountId, AddressDTO addressDTO) {
        List<Address> addresses = addressRepository.findByProfile_Account_Id(accountId);

        Profile profile = profileRepository.findByAccount_Id(accountId)
                .orElseThrow(() -> new ProfileNotFoundException("profile not found for account ID: " + accountId));

        if (addresses.size() >= 2) {
            throw new AddressLimitExceededException("you can't add more than 2 addresses");
        }

        Address address = new Address();
        address.setProfile(profile);
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setZipcode(addressDTO.getZipcode());
        address.setCountry(addressDTO.getCountry());

        Address savedAddress = addressRepository.save(address);

        return mapper.map(savedAddress, AddressDTO.class);
    }

    public AddressDTO updateAddress(int accountId, int addressId, AddressDTO addressDTO) {
        return addressRepository.findById(addressId)
                .filter(address -> address.getProfile().getAccount().getId().equals(accountId))
                .map(address -> {
                    address.setStreet(addressDTO.getStreet());
                    address.setCity(addressDTO.getCity());
                    address.setState(addressDTO.getState());
                    address.setZipcode(addressDTO.getZipcode());
                    address.setCountry(addressDTO.getCountry());
                    return mapper.map(addressRepository.save(address), AddressDTO.class);
                }).orElseThrow(() -> new AddressNotFoundException("address not found for account ID: " + accountId + " and address ID: " + addressId));
    }

    public void deleteAddress(int accountId, int addressId) {
        addressRepository.findById(addressId)
                .filter(address -> address.getProfile().getAccount().getId().equals(accountId))
                .ifPresentOrElse(addressRepository::delete, () -> {
                    throw new AddressNotFoundException("address not found for account ID: " + accountId + " and address ID: " + addressId);
                });
    }

}
