package smoothsail.usecases

interface UseCase<T,U> {
  fun execute(input: T): U
}