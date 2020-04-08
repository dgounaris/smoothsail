package smoothsail.domain.service

import org.springframework.stereotype.Component
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.BuildBranchStatus
import smoothsail.operations.git.GitBranchDetailsRetriever
import smoothsail.repository.BuildBranchDetailsRepository
import smoothsail.tools.SmoothsailClock
import java.time.Clock
import java.time.LocalDateTime

@Component
class LatestBuildBranchDetailsRetriever(
    private val gitBranchDetailsRetriever: GitBranchDetailsRetriever,
    private val buildBranchDetailsRepository: BuildBranchDetailsRepository,
    private val smoothsailClock: SmoothsailClock
) {

  fun retrieve(repository: String, targetBranch: String): BuildBranchDetails {
    val latestPersistedBranch = getLatestPersistedBranch(repository, targetBranch)
    return latestPersistedBranch ?: getAndPersistUpstreamTargetBranch(repository, targetBranch)
  }

  private fun getLatestPersistedBranch(repository: String, targetBranch: String) =
      buildBranchDetailsRepository.findFirstByRepositoryAndTargetBranchAndStatusInOrderByCreatedAtDesc(
          repository,
          targetBranch,
          listOf(BuildBranchStatus.PENDING, BuildBranchStatus.SUCCESS, BuildBranchStatus.MERGED)
      )

  private fun getAndPersistUpstreamTargetBranch(repository: String, targetBranch: String): BuildBranchDetails {
    val upstreamBranchLatestCommitHash = gitBranchDetailsRetriever.retrieve(repository, targetBranch).lastCommitHash
    val upstreamBranchDetails = BuildBranchDetails(
        repository = repository,
        targetBranch = targetBranch,
        currentBuildBranchName = targetBranch,
        buildBranchHash = upstreamBranchLatestCommitHash,
        createdAt = smoothsailClock.now(),
        status = BuildBranchStatus.MERGED
    )
    buildBranchDetailsRepository.save(upstreamBranchDetails)
    return upstreamBranchDetails
  }

}