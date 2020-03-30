package smoothsail.usecases

import org.springframework.stereotype.Component
import smoothsail.domain.BuildBranchStatus

@Component
class UpdateBuildBranchUseCase: UseCase<UpdateBuildBranchUseCase, Unit> { //todo return unit, but throw event for observers to see
  override fun execute(input: UpdateBuildBranchUseCase) {
    TODO("not implemented")
  }
}

class UpdateBuildBranchUseCaseInput (
  val repository: String,
  val buildBranch: String,
  val status: BuildBranchStatus
)