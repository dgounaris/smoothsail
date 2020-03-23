package smoothsail.domain.service

import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.BuildBranchStatus
import smoothsail.operations.git.GitBranchDetailsRetriever
import smoothsail.operations.git.model.GitBranchDetails
import smoothsail.repository.BuildBranchDetailsRepository
import smoothsail.tools.SmoothsailClock
import kotlin.test.assertEquals

internal class LatestBuildBranchDetailsRetrieverTest {

  private val gitBranchDetailsRetriever = mock(GitBranchDetailsRetriever::class.java)
  private val buildBranchDetailsRepository = mock(BuildBranchDetailsRepository::class.java)
  private val smoothsailClock = SmoothsailClock()
  private val latestBuildBranchDetailsRetriever = LatestBuildBranchDetailsRetriever(gitBranchDetailsRetriever, buildBranchDetailsRepository, smoothsailClock)

  @Test
  fun `should return the retrieved item if exists`() {
    val repository = "repo"
    val targetBranch = "target"
    val latestCommitHash = "1q2w3e4r"
    val latestPersisted = BuildBranchDetails(
        repository = repository,
        originBranch = null,
        targetBranch = targetBranch,
        previousBuildBranchDetailsId = null,
        currentBuildBranchName = targetBranch,
        buildBranchHash = latestCommitHash,
        createdAt = smoothsailClock.now(),
        status = BuildBranchStatus.MERGED
    )
    `when`(buildBranchDetailsRepository.findFirstByRepositoryAndTargetBranchAndStatusInOrderByCreatedAtDesc(
        repository, targetBranch, listOf(BuildBranchStatus.PENDING, BuildBranchStatus.SUCCESS, BuildBranchStatus.MERGED)
    )).thenReturn(latestPersisted)

    assertEquals(latestPersisted, latestBuildBranchDetailsRetriever.retrieve(repository, targetBranch))
  }

  @Test
  fun `should return upstream info if no persisted is found`() {
    val repository = "repo"
    val targetBranch = "target"
    val upstreamBranchLatestCommitHash = "1q2w3e4r"
    val latestPersisted = null
    `when`(buildBranchDetailsRepository.findFirstByRepositoryAndTargetBranchAndStatusInOrderByCreatedAtDesc(
        repository, targetBranch, listOf(BuildBranchStatus.PENDING, BuildBranchStatus.SUCCESS, BuildBranchStatus.MERGED)
    )).thenReturn(latestPersisted)
    `when`(gitBranchDetailsRetriever.retrieve(repository, targetBranch)).thenReturn(
        GitBranchDetails(targetBranch, upstreamBranchLatestCommitHash)
    )

    val result = latestBuildBranchDetailsRetriever.retrieve(repository, targetBranch)
    assertEquals(upstreamBranchLatestCommitHash, result.buildBranchHash)
    assertEquals(BuildBranchStatus.MERGED, result.status)
    verify(buildBranchDetailsRepository, times(1)).save(result)
  }

}