package smoothsail.usecases.createbuildbranch

import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.service.JobBuildDetailsSaveService
import smoothsail.domain.service.LatestBuildBranchDetailsRetriever
import smoothsail.domain.service.RebasedBuildBranchGenerator
import kotlin.test.assertEquals

internal class CreateBuildBranchUseCaseTest {

  private val latestBuildBranchDetailsRetriever = mock(LatestBuildBranchDetailsRetriever::class.java)
  private val rebasedBuildBranchGenerator = mock(RebasedBuildBranchGenerator::class.java)
  private val jobBuildDetailsSaveService = mock(JobBuildDetailsSaveService::class.java)
  private val createBuildBranchUseCase = CreateBuildBranchUseCase(latestBuildBranchDetailsRetriever, rebasedBuildBranchGenerator, jobBuildDetailsSaveService)

  @Test
  fun `should create the build branch as expected`() {
    val repo = "repo"
    val target = "target"
    val origin = "origin"
    val jobName = "job"
    val buildNumber = 10L
    val latest = BuildBranchDetails(id = 1L, currentBuildBranchName = "latest")
    val rebased = BuildBranchDetails(id = 2L, currentBuildBranchName = "rebased")
    `when`(latestBuildBranchDetailsRetriever.retrieve(repo, target)).thenReturn(latest)
    `when`(rebasedBuildBranchGenerator.generateAndSave(repo, origin, latest)).thenReturn(rebased)
    val result = createBuildBranchUseCase.execute(CreateBuildBranchUseCaseInput(repo, origin, target, jobName, buildNumber))
    assertEquals("rebased", result)
    Mockito.verify(jobBuildDetailsSaveService, Mockito.times(1)).save(jobName, buildNumber, rebased.id)
  }
}