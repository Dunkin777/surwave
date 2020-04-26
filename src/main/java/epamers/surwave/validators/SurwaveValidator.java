package epamers.surwave.validators;

public interface SurwaveValidator<T> {

  void validate(T entity);
}
