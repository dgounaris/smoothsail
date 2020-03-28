package smoothsail.domain.service

import org.springframework.stereotype.Component
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.BuildBranchStatus
import smoothsail.operations.git.GitBranchRebaseOperator
import smoothsail.operations.git.model.GitBranchDetails
import smoothsail.repository.BuildBranchDetailsRepository
import smoothsail.tools.SmoothsailClock

@Component
class RebasedBuildBranchGenerator(
    private val gitBranchRebaseOperator: GitBranchRebaseOperator,
    private val buildBranchDetailsRepository: BuildBranchDetailsRepository,
    private val smoothsailClock: SmoothsailClock
) {
  fun generate(originBranch: String, latestBuildBranchDetails: BuildBranchDetails): String {
    val rebasedBranchDetails = gitBranchRebaseOperator.operate(
        originBranch,
        latestBuildBranchDetails.currentBuildBranchName,
        "$originBranch-rebasedon-${latestBuildBranchDetails.currentBuildBranchName}"
    )
    return saveToRepository(rebasedBranchDetails, latestBuildBranchDetails).currentBuildBranchName
  }

  private fun saveToRepository(rebasedBranch: GitBranchDetails, latestBuildBranchDetails: BuildBranchDetails) =
      buildBranchDetailsRepository.save(
          BuildBranchDetails(
              repository = latestBuildBranchDetails.repository,
              originBranch = latestBuildBranchDetails.originBranch,
              targetBranch = latestBuildBranchDetails.targetBranch,
              previousBuildBranchDetailsId = latestBuildBranchDetails.id,
              currentBuildBranchName = rebasedBranch.branchName,
              buildBranchHash = rebasedBranch.lastCommitHash,
              createdAt = smoothsailClock.now(),
              status = BuildBranchStatus.PENDING
          )
      )
}