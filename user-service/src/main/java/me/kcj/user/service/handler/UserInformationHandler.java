package me.kcj.user.service.handler;

import lombok.RequiredArgsConstructor;
import me.kcj.user.UserInformation;
import me.kcj.user.UserInformationRequest;
import me.kcj.user.exceptions.UnknownUserException;
import me.kcj.user.repository.PortfolioItemRepository;
import me.kcj.user.repository.UserRepository;
import me.kcj.user.util.EntityMessageMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInformationHandler {

    private final UserRepository userRepository;
    private final PortfolioItemRepository portfolioItemRepository;


    public UserInformation getUserInformation(UserInformationRequest request){
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(()->new UnknownUserException(request.getUserId()));
        var portfolioItem = portfolioItemRepository.findAllByUserId(request.getUserId());
        return EntityMessageMapper.toUserInformation(user, portfolioItem);
    }

}
