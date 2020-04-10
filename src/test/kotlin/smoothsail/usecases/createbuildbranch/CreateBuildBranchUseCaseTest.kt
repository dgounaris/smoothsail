package smoothsail.usecases.createbuildbranch

import org.junit.Test
import org.mockito.Mockito.mock
import smoothsail.domain.service.LatestBuildBranchDetailsRetriever
import smoothsail.domain.service.RebasedBuildBranchGenerator

internal class CreateBuildBranchUseCaseTest {

  private val latestBuildBranchDetailsRetriever = mock(LatestBuildBranchDetailsRetriever::class.java)
  private val rebasedBuildBranchGenerator = mock(RebasedBuildBranchGenerator::class.java)
  private val createBuildBranchUseCase = CreateBuildBranchUseCase(latestBuildBranchDetailsRetriever, rebasedBuildBranchGenerator)

  @Test
  fun `should create the build branch as expected`() {

  }
}