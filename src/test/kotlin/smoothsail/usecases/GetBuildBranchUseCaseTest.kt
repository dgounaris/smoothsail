package smoothsail.usecases

import org.junit.Test
import org.mockito.Mockito.mock
import smoothsail.domain.service.LatestBuildBranchDetailsRetriever
import smoothsail.domain.service.RebasedBuildBranchGenerator

internal class GetBuildBranchUseCaseTest {

  private val latestBuildBranchDetailsRetriever = mock(LatestBuildBranchDetailsRetriever::class.java)
  private val rebasedBuildBranchGenerator = mock(RebasedBuildBranchGenerator::class.java)
  private val getBuildBranchUseCase = GetBuildBranchUseCase(latestBuildBranchDetailsRetriever, rebasedBuildBranchGenerator)

}