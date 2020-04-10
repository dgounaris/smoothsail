package smoothsail.usecases.updatebuildbranch

import org.springframework.stereotype.Component
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.BuildBranchStatus
import smoothsail.domain.service.UpdateBuildBranchActionSelectionStrategy
import smoothsail.repository.BuildBranchDetailsRepository
import smoothsail.repository.JobBuildDetailsRepository
import smoothsail.usecases.UseCase

@Component
class UpdateBuildBranchUseCase(
    private val updateBuildBranchActionSelectionStrategy: UpdateBuildBranchActionSelectionStrategy,
    private val buildBranchDetailsRepository: BuildBranchDetailsRepository,
    private val jobBuildDetailsRepository: JobBuildDetailsRepository
): UseCase<UpdateBuildBranchUseCaseInput, Unit> {
  override fun execute(input: UpdateBuildBranchUseCaseInput) {
    val pendingEntry = getPendingBranchEntry(input.jobName, input.buildNumber)
    updateBuildBranchActionSelectionStrategy.select(input.status).execute(pendingEntry, input.jobName, input.buildNumber)
  }

  private fun getPendingBranchEntry(job: String, build: Long): BuildBranchDetails {
    val buildBranchDetailsId = jobBuildDetailsRepository.findByJobNameAndBuildNumber(job, build)
        ?.buildBranchDetailsId ?: throw NoSuchElementException()

    return buildBranchDetailsRepository.findFirstByIdAndStatusIn(
        buildBranchDetailsId,
        listOf(BuildBranchStatus.PENDING)
    ) ?: throw NoSuchElementException()
  }
}

class UpdateBuildBranchUseCaseInput (
  val jobName: String,
  val buildNumber: Long,
  val status: BuildBranchStatus
)