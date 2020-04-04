package smoothsail.usecases.updatebuildbranch

import org.springframework.stereotype.Component
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.BuildBranchStatus
import smoothsail.domain.service.UpdateBuildBranchActionSelectionStrategy
import smoothsail.repository.BuildBranchDetailsRepository
import smoothsail.usecases.UseCase

@Component
class UpdateBuildBranchUseCase(
    private val updateBuildBranchActionSelectionStrategy: UpdateBuildBranchActionSelectionStrategy,
    private val buildBranchDetailsRepository: BuildBranchDetailsRepository
): UseCase<UpdateBuildBranchUseCaseInput, Unit> {
  override fun execute(input: UpdateBuildBranchUseCaseInput) {
    val pendingEntry = getPendingBranchEntry(input.repository, input.buildBranch)
    updateBuildBranchActionSelectionStrategy.select(input.status).execute(pendingEntry)
  }

  private fun getPendingBranchEntry(repository: String, buildBranch: String): BuildBranchDetails {
    return buildBranchDetailsRepository.findFirstByRepositoryAndCurrentBuildBranchNameAndStatusInOrderByCreatedAtDesc(
        repository,
        buildBranch,
        listOf(BuildBranchStatus.PENDING)
    ) ?: throw NoSuchElementException()
  }
}

class UpdateBuildBranchUseCaseInput (
  val repository: String,
  val buildBranch: String,
  val status: BuildBranchStatus
)