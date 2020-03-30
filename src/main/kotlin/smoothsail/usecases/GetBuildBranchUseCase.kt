package smoothsail.usecases

import org.springframework.stereotype.Component
import smoothsail.domain.service.LatestBuildBranchDetailsRetriever
import smoothsail.domain.service.RebasedBuildBranchGenerator

@Component
class GetBuildBranchUseCase(
    private val latestBuildBranchDetailsRetriever: LatestBuildBranchDetailsRetriever,
    private val rebasedBuildBranchGenerator: RebasedBuildBranchGenerator
) : UseCase<GetBuildBranchUseCaseInput, String> {
  override fun execute(input: GetBuildBranchUseCaseInput): String {
    val latestBuildBranchDetails = latestBuildBranchDetailsRetriever.retrieve(
        repository = input.repository,
        targetBranch = input.targetBranch
    )
    return rebasedBuildBranchGenerator.generate(input.originBranch, latestBuildBranchDetails)
  }
}

data class GetBuildBranchUseCaseInput(
    val repository: String,
    val originBranch: String,
    val targetBranch: String
)