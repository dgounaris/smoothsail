package smoothsail.usecases.createbuildbranch

import org.springframework.stereotype.Component
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.service.JobBuildDetailsSaveService
import smoothsail.domain.service.LatestBuildBranchDetailsRetriever
import smoothsail.domain.service.RebasedBuildBranchGenerator
import smoothsail.usecases.UseCase

@Component
class CreateBuildBranchUseCase(
    private val latestBuildBranchDetailsRetriever: LatestBuildBranchDetailsRetriever,
    private val rebasedBuildBranchGenerator: RebasedBuildBranchGenerator,
    private val jobBuildDetailsSaveService: JobBuildDetailsSaveService
) : UseCase<CreateBuildBranchUseCaseInput, String> {
  override fun execute(input: CreateBuildBranchUseCaseInput): String {
    val latestBuildBranchDetails = latestBuildBranchDetailsRetriever.retrieve(
        repository = input.repository,
        targetBranch = input.targetBranch
    )
    val details = rebasedBuildBranchGenerator.generateAndSave(input.repository, input.originBranch, latestBuildBranchDetails)
    jobBuildDetailsSaveService.save(input.jobName, input.buildNumber, details.id)
    return details.currentBuildBranchName
  }
}

data class CreateBuildBranchUseCaseInput(
    val repository: String,
    val originBranch: String,
    val targetBranch: String,
    val jobName: String,
    val buildNumber: Long
)