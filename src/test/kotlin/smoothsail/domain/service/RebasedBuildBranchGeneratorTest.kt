package smoothsail.domain.service

import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.BuildBranchStatus
import smoothsail.operations.git.GitBranchRebaseOperator
import smoothsail.operations.git.model.GitBranchDetails
import smoothsail.repository.BuildBranchDetailsRepository
import smoothsail.tools.SmoothsailClock
import kotlin.test.assertEquals

internal class RebasedBuildBranchGeneratorTest {
  private val gitBranchRebaseOperator = mock(GitBranchRebaseOperator::class.java)
  private val buildBranchDetailsRepository = mock(BuildBranchDetailsRepository::class.java)
  private val smoothsailClock = SmoothsailClock()
  private val rebasedBuildBranchGenerator = RebasedBuildBranchGenerator(gitBranchRebaseOperator, buildBranchDetailsRepository, smoothsailClock)

  @Test
  fun `should return the rebased branch details after saving the proper info`() {
    val origin = "origin"
    val repository = "repo"
    val targetBranch = "target"
    val latestCommitHash = "1q2w3e4r"
    val latestPersisted = BuildBranchDetails(
        repository = repository,
        targetBranch = targetBranch,
        currentBuildBranchName = targetBranch,
        buildBranchHash = latestCommitHash,
        createdAt = smoothsailClock.now(),
        status = BuildBranchStatus.MERGED
    )
    val newlyPersisted = BuildBranchDetails(
            repository = repository,
            targetBranch = targetBranch,
            currentBuildBranchName = "$origin-rebasedon-${latestPersisted.currentBuildBranchName}",
            buildBranchHash = "q1w2e3r4",
            createdAt = smoothsailClock.now(),
            status = BuildBranchStatus.MERGED
    )
    `when`(gitBranchRebaseOperator.operate(repository, origin, latestPersisted.currentBuildBranchName, "$origin-rebasedon-${latestPersisted.currentBuildBranchName}"))
        .thenReturn(GitBranchDetails("$origin-rebasedon-${latestPersisted.currentBuildBranchName}", "q1w2e3r4"))
    `when`(buildBranchDetailsRepository.save(ArgumentMatchers.any<BuildBranchDetails>())).thenReturn(
        newlyPersisted
    )
    val result = rebasedBuildBranchGenerator.generateAndSave(repository, origin, latestPersisted)
    assertEquals(newlyPersisted, result)
  }
}