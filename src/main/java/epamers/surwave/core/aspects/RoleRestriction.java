package epamers.surwave.core.aspects;

import static epamers.surwave.core.ExceptionMessageContract.USER_HAVE_INSUFFICIENT_PERMISSIONS;

import epamers.surwave.core.annotations.RoleRestricted;
import epamers.surwave.core.exceptions.RoleRestrictionException;
import epamers.surwave.entities.Role;
import epamers.surwave.entities.User;
import epamers.surwave.services.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Aspect
@Component
@RequiredArgsConstructor
public class RoleRestriction {

  private final UserService userService;

  @Around(value = "@annotation(roleRestricted)")
  public Object display(ProceedingJoinPoint joinPoint, RoleRestricted roleRestricted) throws Throwable {
    Role roleFromAnnotation = roleRestricted.role();
    User current = userService.getCurrent();
    Set<Role> currentUserRoles = current.getRoles();

    if (!currentUserRoles.contains(roleFromAnnotation)) {
      throw new RoleRestrictionException(String.format(USER_HAVE_INSUFFICIENT_PERMISSIONS, current.getEmail()));
    }

    return joinPoint.proceed();
  }
}
