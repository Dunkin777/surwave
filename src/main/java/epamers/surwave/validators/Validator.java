package epamers.surwave.validators;

public interface Validator<T> {

  void validate(T entity);
}
